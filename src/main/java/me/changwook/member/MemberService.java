package me.changwook.member;

import lombok.RequiredArgsConstructor;
import me.changwook.member.dto.MemberDTO;
import me.changwook.exception.custom.MemberNotFoundException;
import me.changwook.mapper.MemberMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional(readOnly = true)
    public MemberDTO findById(UUID id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return memberMapper.memberToMemberDTO(member);
    }

    @Transactional
    public void update(UUID memberId, MemberDTO memberDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow( MemberNotFoundException::new);
        //업데이트 할 메서드를 작성
        member.updateMember(memberMapper.memberDTOToMember(memberDTO));
    }

    // 관리자 전용 메서드들
    
    @Transactional(readOnly = true)
    public Page<MemberDTO> getAllMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(memberMapper::memberToMemberDTO);
    }

    @Transactional(readOnly = true)
    public MemberDTO getMemberById(UUID memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        return memberMapper.memberToMemberDTO(member);
    }

    @Transactional
    public MemberDTO updateMemberRole(UUID memberId, Role newRole) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        
        Member updateMember = Member.builder()
            .role(newRole)
            .build();
        
        member.updateMember(updateMember);
        return memberMapper.memberToMemberDTO(member);
    }

    @Transactional
    public void deleteMember(UUID memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public long getTotalMemberCount() {
        return memberRepository.count();
    }

    @Transactional(readOnly = true)
    public MemberDTO findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberNotFoundException::new);
        return memberMapper.memberToMemberDTO(member);
    }

}
