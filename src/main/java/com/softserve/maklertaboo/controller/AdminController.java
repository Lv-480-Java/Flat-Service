package com.softserve.maklertaboo.controller;

import com.softserve.maklertaboo.entity.user.User;
import com.softserve.maklertaboo.mapping.request.RequestForFlatMapper;
import com.softserve.maklertaboo.mapping.request.RequestForUserMapper;
import com.softserve.maklertaboo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.softserve.maklertaboo.utils.DateUtils.asDate;

@CrossOrigin
@RestController
//@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    RequestForFlatMapper requestForFlatMapper;
    RequestForUserMapper requestForUserMapper;
    StatisticsService statisticsService;

    @Autowired
    public AdminController(RequestForUserMapper requestForUserMapper,
                           RequestForFlatMapper requestForFlatMapper,
                           StatisticsService statisticsService) {
        this.requestForFlatMapper = requestForFlatMapper;
        this.requestForUserMapper = requestForUserMapper;
        this.statisticsService = statisticsService;
    }


    @GetMapping("statistics/active-flats")
    public Long getCountOfActiveFlats() {
        return statisticsService.getCountOfActiveFlats();
    }

    @GetMapping("statistics/active-users")
    public Long getCountOfActiveUsers() {
        return statisticsService.getCountOfActiveUsers();
    }

    @GetMapping("statistics/active-landlords")
    public Long getCountOfActiveLandlords() {
        return statisticsService.getCountOfActiveLandlords();
    }


    @GetMapping("statistics/count-comments")
    public List<Long> getCountOfActiveComments() {
        return Arrays.asList(statisticsService.getCountOfActiveFlatsComments(),
                statisticsService.getCountOfActiveUsersComments());
    }


    @GetMapping("statistics/users-landlords")
    public List<Long> getCountOfUsersByRole() {
        return Arrays.asList(statisticsService.getCountOfActiveRenters(),
                statisticsService.getCountOfActiveLandlords(),
                statisticsService.getCountOfActiveModerators());
    }

    @GetMapping("statistics/active-unactive-flats")
    public List<Long> getCountOfActiveUnactiveFlats() {
        return Arrays.asList(statisticsService.getCountOfActiveFlats(),
                statisticsService.getCountOfUnactiveFlats());
    }

    @GetMapping("statistics/user-registration-dynamics/{days}")
    public List<Long> getCountOfRegisteredUsersForLastDays(@PathVariable("days") int days) {
        return statisticsService.getCountOfRegisteredUsersForLastDays(days);
    }

    @GetMapping("statistics/flat-creation-dynamics/{days}")
    public List<Long> getCountOfCreatedFlatsForLastDays(@PathVariable("days") int days) {
        return statisticsService.getCountOfPostedFlatsForLastDays(days);
    }

    @GetMapping("statistics/users-dynamics/{fromMonth}/{toMonth}")
    public List<Long> getCountOfUsersForMount(@PathVariable("fromMonth") String fromMonth,
                                              @PathVariable("toMonth") String toMonth) {
        return statisticsService.getCountOfUsersForBetweenMonths(asDate(fromMonth), asDate(toMonth));
    }

    @GetMapping("statistics/landlords-dynamics/{fromMonth}/{toMonth}")
    public List<Long> getCountOfLandlordsForMount(@PathVariable("fromMonth") String fromMonth,
                                                  @PathVariable("toMonth") String toMonth) {
        return statisticsService.getCountOfLandlordsForBetweenMonths(asDate(fromMonth), asDate(toMonth));
    }

    @GetMapping("statistics/month-names/{fromMonth}/{toMonth}")
    public List<String> getNameOfMonthsInRange(@PathVariable("fromMonth") String fromMonth,
                                               @PathVariable("toMonth") String toMonth) {
        return statisticsService.getNameOfMonthsInRange(asDate(fromMonth), asDate(toMonth));
    }


    @GetMapping("statistics/user-comments-dynamics/{days}")
    public List<Long> getCountOfUserCommentsForLastDays(@PathVariable("days") int days) {
        return statisticsService.getCountOfPostedUsersCommentsFlatsLastDays(days);
    }

    @GetMapping("statistics/flat-comments-dynamics/{days}")
    public List<Long> getCountOfFlatCommentsForLastDays(@PathVariable("days") int days) {
        return statisticsService.getCountOfPostedFlatsCommentsFlatsLastDays(days);
    }

    @GetMapping(value = "statistics/count-posted-flats", params = {"start", "end"})
    public Long getCountOfFlatsBetween(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return statisticsService.getCountOfFlatsBetween(start, end);
    }

//    @GetMapping(value = "statistics/count-orders-between",params = {"start","end"})
//    public Long getCountOfOrdersBetween(@RequestParam Date start,
//                                        @RequestParam Date end){
//        return statisticsService.getCountOfOrdersBetween(start,end);s
//    }

    @GetMapping(value = "statistics/count-posted-comments", params = {"start", "end"})
    public Long getCountOfCommentsBetween(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        return statisticsService.getCountofPostedComments(start, end);
    }

    @GetMapping("/get-top-landlords")
    public List<User> getToplandlords(){
        return statisticsService.getTopLandlords();
    }

}
