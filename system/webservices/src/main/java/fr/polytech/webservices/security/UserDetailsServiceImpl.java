package fr.polytech.webservices.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import fr.polytech.user.components.UserManager;
import fr.polytech.entities.models.Administrator;
 
public class UserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private UserManager uManager;
     
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Administrator admin = uManager.getAdminByEmail(email);
         
        if (admin == null) {
            throw new UsernameNotFoundException("Could not find admin");
        }
         
        return new UserAuthDetails(admin);
    }
 
}