package other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import other.R;
import other.ResultException;

/**
 * 异常处理器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class MyExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**MethodArgumentTypeMismatchException
	 * 处理自定义异常
	 */
	@ExceptionHandler(ResultException.class)
	public R handleRRException(ResultException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getErrMessage());

		//需记录日志
		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return R.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R MethodArgumentNotValidException(MethodArgumentNotValidException e) {
		logger.error(e.getMessage(), e);
		return R.error(404, e.getBindingResult().getFieldError().getDefaultMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public R MethodAccessDeniedException(AccessDeniedException e) {
		logger.error(e.getMessage(), e);
		return R.error(404, "暂无权限,不允许访问");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(NullPointerException.class)
	public R handleDuplicateKeyException(NullPointerException e){
		logger.error(e.getMessage(), e);
		return R.error("空指针异常");
	}

//	@ExceptionHandler(AuthorizationException.class)
//	public R handleAuthorizationException(AuthorizationException e){
//		logger.error(e.getMessage(), e);
//		return R.error("没有权限，请联系管理员授权");
//	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}
}
