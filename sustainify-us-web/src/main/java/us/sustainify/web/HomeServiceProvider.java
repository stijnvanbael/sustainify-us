package us.sustainify.web;

import com.google.sitebricks.At;
import com.google.sitebricks.headless.*;
import com.google.sitebricks.http.Get;
import us.sustainify.common.domain.service.system.SystemSetupService;

import javax.inject.Inject;

@At("/")
@Service
public class HomeServiceProvider {

    private SystemSetupService systemSetupService;

    @Inject
    public HomeServiceProvider(SystemSetupService systemSetupService) {
        this.systemSetupService = systemSetupService;
    }

    @Get
	public Reply<?> get() {
        if(systemSetupService.isSetupRequired()) {
            return Reply.saying().redirect("/setup");
        }
		return Reply.saying().redirect("/authenticated/route-suggestions");
	}
}
