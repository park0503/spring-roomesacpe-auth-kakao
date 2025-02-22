package nextstep.reservation.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.repository.MemberDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.repository.ReservationDao;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.repository.ScheduleDao;
import nextstep.theme.domain.Theme;
import nextstep.theme.repository.ThemeDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;

    public final MemberDao memberDao;

    public Long create(ReservationRequest reservationRequest, Long memberId) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new RoomReservationException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new RoomReservationException(ErrorCode.DUPLICATE_RESERVATION);
        }

        Reservation newReservation = new Reservation(
                schedule,
                reservationRequest.getName(),
                memberId
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new RoomReservationException(ErrorCode.THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id, Long memberId) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        if (!reservation.isMine(memberId)) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }

        reservationDao.deleteById(id);
    }
}
