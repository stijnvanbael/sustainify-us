package us.sustainify.web;

import com.google.sitebricks.At;
import com.google.sitebricks.headless.*;
import com.google.sitebricks.http.Get;

@At("/")
@Service
public class HomeServiceProvider {
	@Get
	public Reply<?> get() {
		return Reply.saying().redirect("/authenticated/route-suggestions");
	}
}
