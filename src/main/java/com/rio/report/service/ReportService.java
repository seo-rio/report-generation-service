package com.rio.report.service;

import com.rio.report.dto.CalloutBlockVo;
import com.rio.report.dto.ContainsConditionVo;
import com.rio.report.dto.DateConditionVo;
import com.rio.report.dto.GenerateReportDto;
import com.rio.report.dto.IconVo;
import com.rio.report.dto.ModifyPeriodDto;
import com.rio.report.dto.NotionResponseDto;
import com.rio.report.dto.OrConditionVo;
import com.rio.report.dto.PeopleConditionVo;
import com.rio.report.dto.RichTextVo;
import com.rio.report.dto.SearchWorkDto;
import com.rio.report.dto.TextVo;
import com.rio.report.dto.block.BlockAnnotationsDto;
import com.rio.report.dto.block.BlockChildrenDto;
import com.rio.report.dto.block.BlockPeopleDto;
import com.rio.report.dto.block.BlockTableCellDto;
import com.rio.report.dto.block.BlockTableRowDto;
import com.rio.report.dto.block.BlockTextDto;
import com.rio.report.dto.db.NotionPersonDto;
import com.rio.report.dto.db.PlanPeriodDto;
import com.rio.report.dto.db.NotionDataDto;
import com.rio.report.dto.db.NotionDataDto.Request;
import com.rio.report.dto.db.WorkResultDto;
import com.rio.report.util.LogUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final WebClient pushWebClient;

    public void generateReport(GenerateReportDto generateReportDto) throws Exception {
       
        NotionResponseDto blockChildren;
        try {
            String retrieveBlockChildUri = "/v1/blocks/" + generateReportDto.getReportPageId() + "/children";
            blockChildren = pushWebClient.get().uri(retrieveBlockChildUri).retrieve().bodyToMono(NotionResponseDto.class).block();
        }catch (Exception e) {
            throw new Exception("Page 정보 조회 실패");
        }

        log.debug(LogUtil.printValue("Block", blockChildren));

        // 1-1. 정보를 조회할 팀원 정보 목록
        ArrayList<String> team = new ArrayList<>();

        String allUsersUri = "/v1/users";
        NotionPersonDto.Response allUsers = pushWebClient.get().uri(allUsersUri).retrieve().bodyToMono(NotionPersonDto.Response.class).block();

        log.debug(LogUtil.printValue("모든 유저", allUsers));

        for (String addr : generateReportDto.getTeamEmailAddr()) {
            for (BlockPeopleDto result : allUsers.getResults()) {
                if(result.getPerson() != null) {
                    if(result.getPerson().get("email").equals(addr)) {
                        team.add(result.getId());
                    }
                }
            }
        }

        // 금주 실적 기간
        Calendar thisWeekStartCal = Calendar.getInstance();
        Calendar thisWeekEndCal = Calendar.getInstance();

        thisWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisWeekEndCal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        // 차주 예정 기간
        Calendar nextWeekStartCal = Calendar.getInstance();
        Calendar nextWeekEndCal = Calendar.getInstance();

        nextWeekStartCal.add(Calendar.DATE, 7);
        nextWeekEndCal.add(Calendar.DATE, 7);

        nextWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        nextWeekEndCal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        // 1. 기간 설정
        List<String> periodCallOutBlockIds = Objects.requireNonNull(blockChildren).getResults().stream()
            .filter(bc -> bc.get("type").equals("callout"))
            .map(f -> f.get("id").toString()).collect(Collectors.toList());

        log.debug(LogUtil.printValue("periodCallOutBlockIds", periodCallOutBlockIds));

        for (int i = 0; i < periodCallOutBlockIds.size(); i++) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            StringBuilder periodSb = new StringBuilder();

            periodSb.append("기간 : ");

            if (i == 0) {
                periodSb.append(format.format(thisWeekStartCal.getTime())).append(" ~ ");
                periodSb.append(format.format(thisWeekEndCal.getTime()));
            } else {
                periodSb.append(format.format(nextWeekStartCal.getTime())).append(" ~ ");
                periodSb.append(format.format(nextWeekEndCal.getTime()));
            }

            String periodCallOutBlockId = periodCallOutBlockIds.get(i);
            String updatePeriodBlockUri = "/v1/blocks/" + periodCallOutBlockId;

            TextVo textVo = new TextVo(periodSb.toString(), null);
            IconVo iconVo = new IconVo("emoji", "\uD83D\uDCC5");
            RichTextVo richTextVo = new RichTextVo("text", textVo);
            CalloutBlockVo calloutBlockVo =
                new CalloutBlockVo(Collections.singletonList(richTextVo), iconVo, "default");

            ModifyPeriodDto modifyPeriodParam = new ModifyPeriodDto();
            modifyPeriodParam.setCallout(calloutBlockVo);

            log.debug(LogUtil.printValue("modifyPeriodParam", modifyPeriodParam));

            try {
                pushWebClient
                    .patch()
                    .uri(updatePeriodBlockUri)
                    .body(Mono.just(modifyPeriodParam), ModifyPeriodDto.class)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            } catch (Exception e) {
                throw new Exception("보고서 기간 설정 실패");
            }
        }

        // 2. 보고서 Table 설정
        List<String> workTableBlockIds = Objects.requireNonNull(blockChildren).getResults().stream()
            .filter(bc -> bc.get("type").equals("table"))
            .map(f -> f.get("id").toString()).collect(Collectors.toList());


        // 2-2. 정보를 조회할 프로젝트 Task DB ID 목록
        ArrayList<String> projectDBIds = new ArrayList<>(generateReportDto.getTaskLinks());

        for (int i = 0; i < workTableBlockIds.size(); i++) {
            for (String projectDBId : projectDBIds) {

                Calendar reqBeforeCal = i == 0 ? thisWeekEndCal : nextWeekEndCal;
                Calendar reqAfterCal = i == 0 ? thisWeekStartCal : nextWeekStartCal;

                // 2-3. 조회 기간 설정
                SimpleDateFormat conditionFormat = new SimpleDateFormat("yyyy-MM-dd");

                DateConditionVo before = new DateConditionVo("계획 기간", new HashMap<>() {{
                    put("on_or_before", conditionFormat.format(reqBeforeCal.getTime()));
                }});

                DateConditionVo after = new DateConditionVo("계획 기간", new HashMap<>() {{
                    put("on_or_after", conditionFormat.format(reqAfterCal.getTime()));
                }});

                ArrayList<Object> masterAndConditionList = new ArrayList<>();

                // 2-4. 조회할 대상 팀원들을 담당자 조건으로 조회
                ArrayList<Object> slaveOrConditionList = new ArrayList<>();
                for (String member : team) {
                    PeopleConditionVo people = new PeopleConditionVo("담당자", new ContainsConditionVo(member));
                    slaveOrConditionList.add(people);
                }

                OrConditionVo slaveOrCondition = new OrConditionVo(slaveOrConditionList);

                masterAndConditionList.add(slaveOrCondition);
                masterAndConditionList.add(before);
                masterAndConditionList.add(after);

                SearchWorkDto searchParam = new SearchWorkDto(new HashMap<>() {{
                    put("and", masterAndConditionList);
                }});

                log.debug(LogUtil.printValue("searchParam", searchParam));

                // 2-5. Task 정보 조회
                NotionDataDto.Response workInfo;
                try {
                    String queryDbUri = "/v1/databases/" + projectDBId + "/query";
                    workInfo = pushWebClient.post().uri(queryDbUri).body(Mono.just(searchParam), SearchWorkDto.class).retrieve()
                        .bodyToMono(NotionDataDto.Response.class).block();

                } catch (Exception e) {
                    throw new Exception("Task 정보 조회 실패");
                }

                log.debug(LogUtil.printValue("workInfo", workInfo));

                NotionDataDto.Request request = new Request();
                List<BlockChildrenDto> reqBlockChildrenList = new ArrayList<>();

                // 3. 조회한 Task 정보를 토대로 보고서 Table에 넣을 Row 데이터 가공
                for (WorkResultDto result : Objects.requireNonNull(workInfo).getResults()) {

                    BlockChildrenDto reqBlockChildren = new BlockChildrenDto();
                    List<List<BlockTableCellDto>> reqBlockTableCells = new ArrayList<>();
                    BlockTableRowDto reqBlockTableRow = new BlockTableRowDto();

                    // 3-1. Task 제목 추출
                    // 제목이 없는 경우는 정상적이지 않은 Task
                    if (result.getProperties().getTitle().getTitle().size() > 0) {
                        String title = result.getProperties().getTitle().getTitle().get(0).getText().getContent();

                        StringBuilder workPerson = new StringBuilder();

                        // 3-2. Task 담당자들 추출
                        List<BlockPeopleDto> people = result.getProperties().getWorkPerson().getPeople();

                        for (int j = 0; j < people.size(); j++) {
                            BlockPeopleDto person = people.get(j);
                            if (j == people.size() - 1) {
                                workPerson.append(person.getName());
                            } else {
                                workPerson.append(person.getName()).append("\n");
                            }
                        }

                        // 3-3. Task 계획 기간 추출
                        PlanPeriodDto planPeriod = result.getProperties().getPlanPeriod();
                        String start = planPeriod.getDate().getStart() == null ? "" : planPeriod.getDate().getStart();
                        String end =
                            planPeriod.getDate().getEnd() == null ? planPeriod.getDate().getStart() == null ? "" : planPeriod.getDate().getStart()
                                : planPeriod.getDate().getEnd();

                        // 3-4. Task 상태 추출
                        String workStatus = result.getProperties().getWorkStatus().getStatus().getName();
                        // 3-5. Task 상태(속성 컬러) 추출
                        String workStatusColor = result.getProperties().getWorkStatus().getStatus().getColor();

                        // 3-6. 보고서 Table Row Cell 가공
                        List<BlockTableCellDto> reqProjectCell = new ArrayList<>();
                        BlockTextDto reqProjectText = new BlockTextDto("프로젝트명", null);
                        BlockAnnotationsDto reqProjectAnnotations = new BlockAnnotationsDto(false, false, false, false, false, "default");
                        reqProjectCell.add(new BlockTableCellDto("text", reqProjectText, reqProjectAnnotations));
                        reqBlockTableCells.add(reqProjectCell);

                        List<BlockTableCellDto> reqWorkDescCell = new ArrayList<>();
                        BlockTextDto reqWorkDescText = new BlockTextDto(title, null);
                        BlockAnnotationsDto reqWorkDescAnnotations = new BlockAnnotationsDto(false, false, false, false, false, "default");
                        reqWorkDescCell.add(new BlockTableCellDto("text", reqWorkDescText, reqWorkDescAnnotations));
                        reqBlockTableCells.add(reqWorkDescCell);

                        List<BlockTableCellDto> reqWorkPersonCell = new ArrayList<>();
                        BlockTextDto reqWorkPersonText = new BlockTextDto(workPerson.toString(), null);
                        BlockAnnotationsDto reqWorkPersonAnnotations = new BlockAnnotationsDto(false, false, false, false, false, "default");
                        reqWorkPersonCell.add(new BlockTableCellDto("text", reqWorkPersonText, reqWorkPersonAnnotations));
                        reqBlockTableCells.add(reqWorkPersonCell);

                        List<BlockTableCellDto> reqWorkStartCell = new ArrayList<>();
                        BlockTextDto reqWorkStartText = new BlockTextDto(start, null);
                        BlockAnnotationsDto reqWorkStartAnnotations = new BlockAnnotationsDto(false, false, false, false, false, "default");
                        reqWorkStartCell.add(new BlockTableCellDto("text", reqWorkStartText, reqWorkStartAnnotations));
                        reqBlockTableCells.add(reqWorkStartCell);

                        List<BlockTableCellDto> reqWorkEndCell = new ArrayList<>();
                        BlockTextDto reqWorkEndText = new BlockTextDto(end, null);
                        BlockAnnotationsDto reqWorkEndAnnotations = new BlockAnnotationsDto(false, false, false, false, false, "default");
                        reqWorkEndCell.add(new BlockTableCellDto("text", reqWorkEndText, reqWorkEndAnnotations));
                        reqBlockTableCells.add(reqWorkEndCell);

                        if (i == 0) {
                            List<BlockTableCellDto> reqWorStatusCell = new ArrayList<>();
                            BlockTextDto reqWorkStatusText = new BlockTextDto(workStatus, null);
                            BlockAnnotationsDto reqWorkStatusAnnotations = new BlockAnnotationsDto(false, false, false, false, false,
                                workStatusColor);
                            reqWorStatusCell.add(new BlockTableCellDto("text", reqWorkStatusText, reqWorkStatusAnnotations));
                            reqBlockTableCells.add(reqWorStatusCell);
                        }

                        reqBlockTableRow.setCells(reqBlockTableCells);
                        reqBlockChildren.setTableRow(reqBlockTableRow);

                        reqBlockChildrenList.add(reqBlockChildren);
                    }
                }

                request.setChildren(reqBlockChildrenList);

                log.debug("Table ID => {}", workTableBlockIds);

                log.debug(LogUtil.printValue("Request", request));

                // 0번째는 금주 실적, 1번째는 차주 실적
                String appendTableBlockUri = "/v1/blocks/" + workTableBlockIds.get(i) + "/children";

                // 4. 보고서 Table Row Block 추가
                try {
                    pushWebClient
                        .patch()
                        .uri(appendTableBlockUri)
                        .body(Mono.just(request), NotionDataDto.Request.class)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                } catch (Exception e) {
                    throw new Exception("보고서 정보 등록 실패");
                }

            }

        }


    }
}


