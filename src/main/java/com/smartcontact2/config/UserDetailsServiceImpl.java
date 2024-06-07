package com.smartcontact2.config;

import com.smartcontact2.dao.UserRepository;
import com.smartcontact2.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //fetching user from database
        User user = userRepository.getUserByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("could not found user !!");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails;
    }
}
