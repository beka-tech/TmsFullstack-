package et.tms.api.enrollment;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
//@Scope(WebApplicationContext.SCOPE_REQUEST) // one per HTTP request, NO proxy and fail at start up But prototype run and fail silently
@Scope("prototype") // one per HTTP request, NO proxy and fail at start up But prototype run and fail silently
public class EnrollmentBatch {

    private final List<String> processed = new ArrayList<>();

    public void add (String item) {processed.add(item);}
    public int size() {return processed.size();}
}