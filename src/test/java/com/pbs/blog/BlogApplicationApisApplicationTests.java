package com.pbs.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pbs.blog.repositories.UserRepository;

@SpringBootTest
class BlogApplicationApisApplicationTests {

	@Autowired
	private UserRepository userRepo;
	
	@Test
	void contextLoads() {
	}

	@Test
	public void repoTest() {
		/*now this userRepo has which class inside it , we can fetch via reflextion api concept of java*/
		String className = this.userRepo.getClass().getName();
		String packageName = this.userRepo.getClass().getPackageName();
		System.out.println(className+": is the className of userRepo and its package name is:"+packageName);
	}
}
