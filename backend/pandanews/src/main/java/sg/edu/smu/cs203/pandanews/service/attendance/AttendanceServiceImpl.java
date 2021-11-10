package sg.edu.smu.cs203.pandanews.service.attendance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.edu.smu.cs203.pandanews.model.attendance.Attendance;
import sg.edu.smu.cs203.pandanews.repository.AttendanceRepository;
import sg.edu.smu.cs203.pandanews.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepo;
    @Autowired
    UserRepository userRepo;


    @Override
    public Attendance punchInOrOut(Long userId) {
        Optional<Attendance> temp = attendanceRepo.findByDate(userId, LocalDate.now());
        if (temp.isPresent() && temp.get().isPunchedOut()) {
            return null;
        }

        Attendance attendance = temp.orElse(new Attendance());
        if (temp.isPresent()) {
            attendance.setPunchedOut(true);
            attendance.setPunchOutDate(LocalDate.now());
            attendance.setPunchOutTime(LocalTime.now());
        } else {
            attendance.setPunchInDate(LocalDate.now());
            attendance.setPunchInTime(LocalTime.now());
        }
        attendance.setUser(userRepo.findById(userId).orElse(null));
        return attendanceRepo.save(attendance);
    }

    // @Override
    // public Attendance updateAttendance(Long id, LocalTime time) {
    //     Attendance a = attendanceRepository.findById(id).orElse(null);
    //     if (a == null) return null;
    //     a.setATime(time);
    //     return attendanceRepository.save(a);

    // }

    @Override
    public List<Attendance> findAttendancesByUserid(Long userId) {
        return attendanceRepo.findByUser(userRepo.findById(userId).orElse(null));
    }

    @Override
    public Attendance findAttendanceByDate(Long userId, LocalDate date) {
        return attendanceRepo.findByDate(userId, date).orElse(null);
    }

    @Override
    public Attendance findAttendanceById(Long id) {
        return attendanceRepo.findById(id).orElse(null);
    }
}
