package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Member;
import aloha.domain.MemberAuth;
import aloha.domain.MemberImg;
import aloha.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper mapper;

	@Override
	public void register(Member member) throws Exception {
		mapper.create(member);
		member = mapper.read(member.getUserId());
		
		MemberAuth memberAuth = new MemberAuth();
		memberAuth.setUserNo(member.getUserNo());
		memberAuth.setAuth("ROLE_USER");
		
		mapper.createAuth(memberAuth);
	}

	@Override
	public List<Member> list() throws Exception {
		return mapper.list();
	}

	@Override
	public Member read(String userId) throws Exception {
		return mapper.read(userId);
	}

	@Override
	public void updateProfile(MemberImg img) throws Exception {
		// 대표 프로필 사진이 있는지?
		int profileCount = mapper.checkProfile(img);
		img.setCategory("thumbnail");
		
		boolean checkProfile = profileCount > 0 ? true : false;
		
		// 대표 프로필 있으면 update
		if( checkProfile ) {
			// 새로 변경할 이미지는 추가.
			// 기존 대표 프로필은 category 데이터를 thumbnail에서 img로 변경
			mapper.updateProfile(img);
		}
		mapper.insertProfile(img);
	}
	
	
}
