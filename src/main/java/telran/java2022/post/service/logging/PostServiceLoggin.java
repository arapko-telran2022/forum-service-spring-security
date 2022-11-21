package telran.java2022.post.service.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Service
@Slf4j(topic = "Post service")
public class PostServiceLoggin {
	
	@Pointcut("execution(* telran.java2022.post.service.PostServiceImpl.getPost(String)) && args(id)")
	public void findById(String id) {}
	
	@Before("findById(id)")
	public void getPostLogging(String id) {
		log.info("post with id {} requested", id);
	}
	
	@Pointcut("execution(public Iterable<telran.java2022.post.dto.PostDto> telran.java2022.post.service.PostServiceImpl.findPosts*(..))")
	public void bulkinFind() {}
	
	@Around("bulkinFind()")
	public Object bulkinFindLogging(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		Long t1 = System.currentTimeMillis();
		Object retVal =  pjp.proceed(args);
		Long t2 = System.currentTimeMillis();
		Long duration= (t2-t1);
		log.info("method - {}, duration = {}", pjp.getSignature().toLongString(), "" + (duration>=1000?duration/1000 + " sec":duration + " ms"));
		return retVal;
	}
	
	@Pointcut("@annotation(PostLogger) && args(.., id)")
	public void anotated(String id) {}
	
	@AfterReturning("anotated(id)")
	public void changePostLogging(String id) {
		log.info("post with id {} changed", id);
	}

}
