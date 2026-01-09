package com.hkorea.skyisthelimit.service;

import static com.hkorea.skyisthelimit.repository.predicate.MemberProblemPredicates.usernameEq;

import com.hkorea.skyisthelimit.common.exception.BusinessException;
import com.hkorea.skyisthelimit.common.response.ErrorCode;
import com.hkorea.skyisthelimit.common.utils.PromptUtil;
import com.hkorea.skyisthelimit.common.utils.QueryDSLHelper;
import com.hkorea.skyisthelimit.common.utils.mapper.MemberProblemMapper;
import com.hkorea.skyisthelimit.dto.criteria.Criteria;
import com.hkorea.skyisthelimit.dto.criteria.PageableCriteria;
import com.hkorea.skyisthelimit.dto.memberproblem.request.MemberProblemTagCountResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveRequest;
import com.hkorea.skyisthelimit.dto.memberproblem.request.SolveResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.MemberProblemResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.NoteResponse;
import com.hkorea.skyisthelimit.dto.memberproblem.response.RandomProblemResponse;
import com.hkorea.skyisthelimit.dto.prompt.IncorrectSummaryDTO;
import com.hkorea.skyisthelimit.entity.Member;
import com.hkorea.skyisthelimit.entity.MemberProblem;
import com.hkorea.skyisthelimit.entity.Problem;
import com.hkorea.skyisthelimit.entity.QMemberProblem;
import com.hkorea.skyisthelimit.entity.QProblem;
import com.hkorea.skyisthelimit.entity.WrongReason;
import com.hkorea.skyisthelimit.entity.embeddable.ProblemTag;
import com.hkorea.skyisthelimit.entity.embeddable.QProblemTag;
import com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus;
import com.hkorea.skyisthelimit.repository.MemberProblemRepository;
import com.hkorea.skyisthelimit.service.enums.ResultCategory;
import com.hkorea.skyisthelimit.service.enums.SolveStatus;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProblemService {


  private final MemberService memberService;
  private final ProblemService problemService;
  private final QueryDSLHelper queryDSLService;
  private final JPAQueryFactory queryFactory;

  private final MemberProblemRepository memberProblemRepository;
  private final OpenAiService  openAiService;

  @Transactional
  public Page<MemberProblemResponse> getMemberProblemPage(
      String username,
      PageableCriteria<QMemberProblem> criteria
  ) {

    memberService.getMember(username);

    QMemberProblem mp = QMemberProblem.memberProblem;
    QProblem p = QProblem.problem;

    BooleanExpression predicate =
        criteria.toPredicate().and(mp.member.username.eq(username));
    OrderSpecifier<?> orderSpecifier = criteria.toOrderSpecifier(mp);
    Pageable pageable = criteria.toPageable();

    List<MemberProblem> memberProblemList = queryFactory
        .selectFrom(mp)
        .join(mp.problem,p).fetchJoin()
        .where(predicate)
        .orderBy(orderSpecifier)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    List<MemberProblemResponse> memberProblemResponseList =
        MemberProblemMapper.toMemberProblemResponseList(memberProblemList);

    long total = queryDSLService.fetchTotalCount(mp, predicate);

    return new PageImpl<>(memberProblemResponseList, pageable, total);
  }

  @Transactional
  public SolveResponse solveProblem(String username, SolveRequest requestDTO) {

    Member member = memberService.getMember(username);
    Problem problem = problemService.getOrRegisterProblem(requestDTO.getBaekjoonId());

    MemberProblem memberProblem = handleSolve(member, problem, requestDTO.getSubmitId(),
        requestDTO.getIsSolved());

    if(requestDTO.getResultCategory() == ResultCategory.WA){

      List<String> tagNames = problem.getProblemTagList().stream()
          .map(ProblemTag::getKoName)
          .toList();

      IncorrectSummaryDTO incorrectSummaryDTO = new IncorrectSummaryDTO(
          requestDTO.getBaekjoonId().toString(),
          requestDTO.getProblemDescription(),
          requestDTO.getProblemInput(),
          requestDTO.getProblemOutput(),
          requestDTO.getCode(),
          tagNames
      );

      String generatedReasonByAi = openAiService.generate(PromptUtil.createIncorrectSummaryPrompt(incorrectSummaryDTO));

      WrongReason wrongReason =  new WrongReason(memberProblem ,generatedReasonByAi);

      memberProblem.getWrongReasons().add(wrongReason);
    } else {
      WrongReason wrongReason = new WrongReason(memberProblem,
          requestDTO.getResultCategory().getDescription());

      memberProblem.getWrongReasons().add(wrongReason);
    }

    return MemberProblemMapper.toSolveResponse(memberProblem);
  }

  @Transactional
  public RandomProblemResponse getRandomProblem(String username,
      Criteria<QMemberProblem> criteria) {

    QMemberProblem memberProblem = QMemberProblem.memberProblem;

    BooleanExpression predicate = criteria.toPredicate().and(usernameEq(username));

    MemberProblem randomMemberProblem = pickRandom(
        queryDSLService.fetchEntities(memberProblem, predicate));

    return MemberProblemMapper.toRandomProblemResponse(randomMemberProblem);
  }

  @Transactional
  public NoteResponse getNote(String username, Integer baekjoonId) {

    MemberProblem memberProblem = getMemberProblem(username, baekjoonId);

    return MemberProblemMapper.toNoteResponse(memberProblem);
  }

  @Transactional
  public NoteResponse writeNote(String username, Integer baekjoonId, String content) {

    Member member = memberService.getMember(username);
    MemberProblem memberProblem = getMemberProblem(username, baekjoonId);

    member.recordNotesWritten();
    memberProblem.writeNote(content);

    return MemberProblemMapper.toNoteResponse(memberProblem);
  }

  @Transactional
  public List<MemberProblemTagCountResponse> getMemberStatistics(String username) {

    List<MemberProblem> memberProblems = memberProblemRepository.findByMemberUsername(username);

    return MemberProblemMapper.toMemberProblemTagCountResponseList(memberProblems);

  }

  private MemberProblem findMemberProblem(String username, Integer baekjoonId) {
    return memberProblemRepository.findByMemberUsernameAndProblemBaekjoonId(username, baekjoonId)
        .orElse(null);
  }

  private MemberProblem getMemberProblem(String username, Integer baekjoonId) {

    return memberProblemRepository.findByMemberUsernameAndProblemBaekjoonId(username, baekjoonId)
        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_PROBLEM_NOT_FOUND));
  }

  private MemberProblem handleSolve(Member member, Problem problem, Long submitId,
      boolean isSolved) {

    MemberProblem memberProblem = findMemberProblem(member.getUsername(), problem.getBaekjoonId());

    SolveStatus solveStatus = SolveStatus.of(memberProblem, submitId, isSolved);

    return switch (solveStatus) {

      case NOTHING -> memberProblem;
      case CORRECT_NOT_TRIED -> handleCorrectNotTried(member, problem, submitId);
      case CORRECT_ALREADY_SOLVED -> handleCorrectAlreadySolved(memberProblem, submitId);
      case CORRECT_ALREADY_UNSOLVED -> handleCorrectAlreadyUnsolved(memberProblem, submitId);
      case WRONG_NOT_TRIED -> handleWrongNotTried(member, problem, submitId);
      case WRONG_ALREADY_TRIED -> handleWrongAlreadyTried(memberProblem, submitId);
    };

  }

  private MemberProblem handleCorrectNotTried(Member member, Problem problem, Long submitId) {

    MemberProblem memberProblem = MemberProblem.create(member, problem, MemberProblemStatus.SOLVED);
    memberProblem.setLastSubmitId(submitId);
    memberProblem.incrementSolvedCount();

    member.recordNewProblemSolved(memberProblem);

    return memberProblem;

  }

  private MemberProblem handleCorrectAlreadySolved(MemberProblem memberProblem, Long submitId) {
    memberProblem.setLastSubmitId(submitId);
    memberProblem.incrementSolvedCount();
    memberProblem.setSolvedDate(LocalDate.now());

    return memberProblem;
  }

  private MemberProblem handleCorrectAlreadyUnsolved(MemberProblem memberProblem, Long submitId) {

    memberProblem.setLastSubmitId(submitId);
    memberProblem.setStatus(MemberProblemStatus.MULTI_TRY);
    memberProblem.setSolvedDate(LocalDate.now());
    memberProblem.incrementSolvedCount();

    memberProblem.getMember().recordNewProblemSolved(memberProblem);

    return memberProblem;
  }

  private MemberProblem handleWrongNotTried(Member member, Problem problem, Long submitId) {

    MemberProblem memberProblem = MemberProblem.create(member, problem,
        MemberProblemStatus.UNSOLVED);

    memberProblem.setLastSubmitId(submitId);
    member.recordNewProblemUnsolved(memberProblem);
    return memberProblem;
  }

  private MemberProblem handleWrongAlreadyTried(MemberProblem memberProblem, Long submitId) {
    memberProblem.setLastSubmitId(submitId);
    return memberProblem;
  }

  private MemberProblem pickRandom(List<MemberProblem> memberProblems) {

    if (memberProblems == null || memberProblems.isEmpty()) {
      return null;
    }

    Random random = new Random();
    return memberProblems.get(random.nextInt(memberProblems.size()));
  }

}
