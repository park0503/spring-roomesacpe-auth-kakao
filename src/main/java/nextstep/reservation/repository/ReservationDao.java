package nextstep.reservation.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.domain.Reservation;
import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationDao {

    private static final String SELECT_SQL =
            "SELECT reservation.id, reservation.schedule_id, reservation.name, reservation.member_id, schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price "
                    +
                    "from reservation " +
                    "inner join schedule on reservation.schedule_id = schedule.id " +
                    "inner join theme on schedule.theme_id = theme.id ";

    public final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("reservation.id"),
            new Schedule(
                    resultSet.getLong("schedule.id"),
                    new Theme(
                            resultSet.getLong("theme.id"),
                            resultSet.getString("theme.name"),
                            resultSet.getString("theme.desc"),
                            resultSet.getInt("theme.price")
                    ),
                    resultSet.getDate("schedule.date").toLocalDate(),
                    resultSet.getTime("schedule.time").toLocalTime()
            ),
            resultSet.getString("reservation.name"),
            resultSet.getLong("reservation.member_id")
    );

    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, name, member_id) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setString(2, reservation.getName());
            ps.setLong(3, reservation.getMemberId());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql = SELECT_SQL +
                "where theme.id = ? and schedule.date = ?;";
        return jdbcTemplate.query(sql, rowMapper, themeId, Date.valueOf(date));
    }

    public Reservation findById(Long id) {
        String sql = SELECT_SQL +
                "where reservation.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByScheduleId(Long id) {
        String sql = SELECT_SQL +
                "where schedule.id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByMemberId(Long id) {
        String sql = SELECT_SQL +
                "where reservation.member_id = ?;";
        try {
            return jdbcTemplate.query(sql, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation where id = ?;";
        jdbcTemplate.update(sql, id);
    }
}
