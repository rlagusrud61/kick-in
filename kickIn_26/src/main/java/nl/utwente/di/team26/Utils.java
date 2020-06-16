package nl.utwente.di.team26;

import javax.ws.rs.core.SecurityContext;

public class Utils {

    public static long getUserFromContext(SecurityContext securityContext) {
        return Long.parseLong(securityContext.getUserPrincipal().getName());
    }

}
