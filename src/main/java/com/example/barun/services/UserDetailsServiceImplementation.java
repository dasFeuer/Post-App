package com.example.barun.services;

import com.example.barun.entities.userEntities.User;
import com.example.barun.entities.userEntities.UserPrincipal;
import com.example.barun.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrincipal(user);
    // Will need Username for login and other stuff where username required
    }
}

//    @Override
//    public UserDetails loadUserByUsername(String email) throws BadCredentialsException {
//        User user = userRepository.findByEmail(email).
//                orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new UserPrincipal(user);
//        }     // Will need Username for login and other stuff where username required