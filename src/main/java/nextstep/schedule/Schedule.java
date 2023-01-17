package nextstep.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.theme.Theme;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private Long id;
    private Theme theme;
    private LocalDate date;
    private LocalTime time;

    public Schedule(Theme theme, LocalDate date, LocalTime time) {
        this(null, theme, date, time);
    }
}
