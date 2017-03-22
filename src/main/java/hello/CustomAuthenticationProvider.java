package hello;


import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


/**
 * @author nokutu
 * @since 22/03/2017.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL("APIURL");
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            JSONObject object = new JSONObject(result.toString());
            // TODO parse
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new UserAuthority());
            return new UsernamePasswordAuthenticationToken(userName, password, authorities);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private class UserAuthority implements GrantedAuthority {

        @Override
        public String getAuthority() {
            return "USER";
        }
    }

    private class PersonnelAuthority implements GrantedAuthority {

        @Override
        public String getAuthority() {
            return "PERSONNEL";
        }
    }
}
