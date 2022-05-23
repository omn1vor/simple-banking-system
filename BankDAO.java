package banking;

import java.sql.*;
import java.util.Optional;

public class BankDAO {
    private final DB db;

    public BankDAO(DB db) {
        this.db = db;
    }

    public long count() {
        String sql = """
                SELECT COUNT(*) as count
                FROM card;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             Statement statement = con.createStatement();
             ResultSet rst = statement.executeQuery(sql)) {

            return rst.getInt("count");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Optional<Card> get(String number, String pin) {
        String sql = """
                SELECT number, pin, balance
                FROM card
                WHERE number = ? AND pin = ?;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, number);
            statement.setString(2, pin);

            try (ResultSet rst = statement.executeQuery()) {

                if (rst.next()) {
                    return Optional.of(new Card(
                            rst.getString("number"),
                            rst.getString("pin"),
                            rst.getInt("balance")));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public boolean exists(String number) {
        String sql = """
                SELECT number
                FROM card
                WHERE number = ?;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, number);
            try (ResultSet rst = statement.executeQuery()) {
                return rst.next();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void addCard(Card card) {
        String sql = """
                INSERT INTO card (number, pin)
                VALUES (?, ?);
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, card.getNumber());
            statement.setString(2, card.getPin());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public long balance(Card card) {
        String sql = """
                SELECT balance
                FROM card
                WHERE number = ?;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, card.getNumber());
            ResultSet rst = statement.executeQuery();
            return rst.next() ? rst.getLong("balance") : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deposit(Card card, long sum) {
        String sql = """
                UPDATE card
                SET balance = balance + ?
                WHERE number = ?;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setLong(1, sum);
            statement.setString(2, card.getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void transfer(Card from, String to, long sum) {
        String withdrawSql = """
                UPDATE card
                SET balance = balance - ?
                WHERE number = ?;
                """;
        String depositSql = """
                UPDATE card
                SET balance = balance + ?
                WHERE number = ?;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl())) {
            con.setAutoCommit(false);
            try (PreparedStatement withdraw = con.prepareStatement(withdrawSql);
                 PreparedStatement deposit = con.prepareStatement(depositSql)) {

                withdraw.setLong(1, sum);
                withdraw.setString(2, from.getNumber());
                withdraw.executeUpdate();

                deposit.setLong(1, sum);
                deposit.setString(2, to);
                deposit.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void delete(Card card) {
        String sql = """
                DELETE FROM card
                WHERE number = ?;
                """;
        try (Connection con = DriverManager.getConnection(db.getUrl());
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, card.getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
