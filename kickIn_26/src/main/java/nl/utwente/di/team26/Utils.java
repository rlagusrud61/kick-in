package nl.utwente.di.team26;

import javax.ws.rs.core.SecurityContext;

public class Utils {

    public static int getUserFromContext(SecurityContext securityContext) {
        return Integer.parseInt(securityContext.getUserPrincipal().getName());
    }

}
