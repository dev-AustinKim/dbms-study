package view;

import dao.MyUserDAO;
import domain.UserVO;

public class MyTest {
	public static void main(String[] args) {
		MyUserDAO userDAO = new MyUserDAO();
		UserVO userVO = new UserVO();
		//	���̵� �ߺ��˻� o
//		if(userDAO.checkId("hds1234")) {
//			System.out.println("��� ������ ���̵�");
//			return;
//		}
//		System.out.println("�ߺ��� ���̵�");

//		System.out.println(userDAO.select(1L));
		//	�α��� o
//		System.out.println(userDAO.login("kdh1234", "1234"));

		//	ȸ������ o
		userVO.setUserIdentification("kdh5678");
		userVO.setUserName("�赿��2");
		userVO.setUserPassword("1111");
		userVO.setUserPhone("01022222222");
		userVO.setUserNickname("��");
		userVO.setUserEmail("dsa@gamil.com");
		userVO.setUserAddress("���� ���ı�");
		userVO.setUserBirth("1998-05-12");
		userVO.setUserGender("W");
		userVO.setUserRecommenderId("kdh1234");
		userDAO.signUp(userVO);

		//	��ȣȭ	o	
//		System.out.println(userDAO.encryptPassword("1234"));

		//	��й�ȣ ���� o
//		userDAO.changePassword("kdh1234", "1234");


		//	ȸ������ ���� o
//      userVO.setUserName("���ο�");
//      userVO.setUserPassword("5678");
//      userVO.setUserPhone("01098765431");
//      userVO.setUserNickname("��");
//      userVO.setUserEmail("ymw5678@gmail.com");
//      userVO.setUserAddress("����� ���ı�");
//      userVO.setUserId(2L);
//      userDAO.update(userVO);

		//	ȸ������ ��ȸ O
//		System.out.println(userDAO.select(2L));
		
		//   ȸ��Ż�� O
//		userDAO.delete(3L);

		//	���̵� ã�� O
//		System.out.println(userDAO.findId("01012341234"));

		// ���� ��õ�� ��� o
//		System.out.println(userDAO.recommendedMe("hds1234"));

		// ���� ��õ�� ��� o
//		System.out.println(userDAO.recommendMe("hds1234"));

		// ��õ�� o
//		System.out.println(userDAO.countRecommend("hds1234"));



	}
}
