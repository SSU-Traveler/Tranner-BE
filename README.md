# Tranner
# Develop 

#h2 241015 수정 내용
1. schedule.controller의 ScheduleController에 신규 스케줄을 생성하는
   public ResponseEntity<String> addSchedule(HttpServletRequest request,
                                              @Validated @RequestBody AddScheduleRequest scheduleRequest)
   작성
2. schedule.dto에 각종 dto 작성
3. schedule.service의 ScheduleServie에 해당 멤버에 스케줄을 저장 및 일자별 스케줄도 저장하는
   public void saveSchedule(String username, AddScheduleRequest newScheduleDTO)
   작성
5. schedule.service의 ScheduleServie에 해당 멤버의 모든 스케줄을 리턴하는
   public List<FindScheduleDTO> findAllSchedules(String username)
   작성
6. schedule.repository의 ScheduleRepository에 username으로 해당 유저의 모든 스케줄을 조회하는 메소드
   List<Schedule> findAllByMember_Username(String username)
   작성
