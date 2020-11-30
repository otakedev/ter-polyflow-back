package fr.polytech.webservices.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import fr.polytech.user.components.UserManager;
import fr.polytech.workflow.models.User;
 
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private UserManager uManager;
     
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = uManager.getUserByEmail(email);
         
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new UserAuthDetails(user);
    }
 
}