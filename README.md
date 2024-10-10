# Tranner
# Develop 

### 241005 수정 내용
1. schedule.controller의 ScheduleController에 신규 스케줄을 생성하는
   ```
   public ResponseEntity<String> addSchedule(HttpServletRequest request,
                                              @Validated @RequestBody AddScheduleRequest scheduleRequest)
   ```
2. schedule.dto에 각종 dto 작성
3. schedule.service의 ScheduleServie에 해당 멤버에 스케줄을 저장 및 일자별 스케줄도 저장하는
   ```
   public void saveSchedule(String username, AddScheduleRequest newScheduleDTO)
   ```
4. schedule.service의 ScheduleServie에 해당 멤버의 모든 스케줄을 리턴하는
   ```
   public List<FindScheduleDTO> findAllSchedules(String username)
   ```
5. schedule.repository의 ScheduleRepository에 username으로 해당 유저의 모든 스케줄을 조회하는 메소드
   ```
   List<Schedule> findAllByMember_Username(String username)
   ```
### 241006 수정 내용
1. schedule.dto에 기존 스케줄 수정시 받은 스케줄을 담는 dto EditScheduleRequest 생성
2. db에 찾고자 하는 스케줄이 없을 때, 발생하는 exception ScheduleNotFoundException
3. schedule.controller의 ScheduleController에 기존 스케줄을 수정하는
   ```
   public ResponseEntity<String> editSchedule(HttpServletRequest request,
                                               @Validated @RequestBody EditScheduleRequest scheduleRequest)
   ```
4. schedule.service의 ScheduleService에 해당 멤버의 기존 스케줄을 수정하는 메소드
   ```
   public void editSchedule(String username, EditScheduleRequest editScheduleDTO)
   ```
### 241007 수정 내용
1. schedule.controller의 editSchedule()에서, 쿼리파라미터를 사용하는 방식으로 변경함.

### 241010 수정 내용
1. member 프로필 수정을 위한 dto MemberEditRequest, MemberEditPageRequest 생성
2. member 프로필 수정 페이지를 위한 controller MemberController에 edit() 추가
   ```
   @GetMapping
    public ResponseEntity<MemberEditPageResponse> edit(HttpServletRequest request)
   ```
3. member 프로필 수정을 위한 controller MemberController에 edit() 추가
   ```
   @PatchMapping
    public ResponseEntity<Void> edit(HttpServletRequest request,
                       @Validated @RequestBody MemberEditRequest memberEditRequest)
   ```
4. member 프로필 수정을 위한 MemberService에 editMember() 추가
   ```
   @Transactional
    public boolean editMember(String username,
                              MemberEditRequest memberEditRequest)
   ```