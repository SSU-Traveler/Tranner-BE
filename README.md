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

### 241118 수정 내용
1. user가 창을 닫거나, 로그아웃 했을 때, 북마크(찜), 장바구니 리스트를 저장을 위해 controller에 
   ```
   @PostMapping("/saveUserInfo")
   public ResponseEntity<String> saveUserInfo(HttpServletRequest request,
   @RequestBody SaveUserInfoRequest saveUserInfoRequest)
   ```
   추가
   왜냐하면 user 가 사용 중간에 내용(찜, 장바구니)을 변경했을 테니까...
2. 북마크, 장바구니 리스트 요청에 대한 dto
   BookmarkRequest, CandidateLocationRequest, SaveUserInfoRequest를 dto의 request에 추가
3. user의 북마크, 장바구니 리스트를 수정하기 위한 service 메소드를 MemberService에 추가
   ```
   public void saveUserData(String username,
                             SaveUserInfoRequest saveUserInfoRequest)
   ```
4. user의 모든 북마크 삭제, 장바구니 삭제 메소드를 /member/domain/Member에 추가
   ```
   public void deleteAllBookmarks()
   public void deleteAllCandidateLocations()
   ```

