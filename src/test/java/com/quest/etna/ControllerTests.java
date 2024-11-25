package com.quest.etna;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.JwtUserDetails;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtUtils;

    @Sql(scripts={"classpath:database/populated-data.sql"})
    @Order(1)
    @Test
    public void testAuthenticate() throws Exception {
        /*
         * Register user
         * Should be 201 Created
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastname\": \"TRENET\", " +
                                "\"firstname\": \"Nathan\"," +
                                "\"username\": \"trenet_n\", " +
                                "\"email\": \"trenet_n@etna-alternance.net\", " +
                                "\"password\": \"password\"}")
                )
                .andExpect(status().isCreated());

        /*
         * Register duplicate user
         * Should be 409 Conflict
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastname\": \"RINAZ\", " +
                                "\"firstname\": \"Samir\"," +
                                "\"username\": \"rinaz_s\", " +
                                "\"email\": \"rinaz_s@etna-alternance.net\", " +
                                "\"password\": \"password\"}")
                )
                .andExpect(status().isConflict());

        /*
         * Authenticate
         * Should be 200 Ok
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"trenet_n\", " +
                                "\"password\": \"password\"}")
                )
                .andExpect(content().string(containsString("token")))
                .andExpect(status().isOk());

        /*
         * Authenticate with bad password
         * Should be 401 Unauthorized
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"trenet_n\", " +
                                "\"password\": \"rocketleague>ALL\"}")
                )
                .andExpect(status().isUnauthorized());

        /*
         * Authenticated user information
         * Should be 200 Ok
         * */
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername("trenet_n");
        String token = jwtUtils.generateToken(jwtUserDetails);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/me")
                        .header("Authorization" , "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    public void testUser() throws Exception {
        /*
         * Generate an user jwt token
         * */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("trenet_n", "password"));

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());
        String userToken = jwtUtils.generateToken(jwtUserDetails);

        /*
         * Get all registered user's information
         * Should be 200 Ok
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/user")
                        .header("Authorization" , "Bearer " + userToken))
                .andExpect(status().isOk());

        /*
         * Delete user with id 1 as a simple user
         * Should be 401 Unauthorized
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/1")
                        .header("Authorization" , "Bearer " + userToken))
                .andExpect(status().isUnauthorized());

        /*
         * Get all user's without Bearer token
         * Should be 401 Unauthorized
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isUnauthorized());

        /*
         * Generate an admin jwt token
         * */
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("rinaz_s", "password"));

        jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());
        String adminToken = jwtUtils.generateToken(jwtUserDetails);

        /*
         * Delete user with id 3 as admin
         * Should be 200 Ok
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/user/2")
                        .header("Authorization" , "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Order(3)
    @Test
    public void testCategories() throws Exception {
        /*
         * Auth Admin
         * */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("rinaz_s", "password"));

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());
        String adminToken = jwtUtils.generateToken(jwtUserDetails);

        /*
         * Post an category as admin (user.id 1)
         * Should be 201 Created
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + adminToken)
                        .content("{\"name\": \"Rinaz Category\", " +
                                "\"description\": \"Some lessons about development\"}"))
                .andExpect(status().isCreated());

        /*
         * Generate an user jwt token
         * */
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("trenet_n", "password"));

        jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());
        String userToken = jwtUtils.generateToken(jwtUserDetails);

        /*
         * Post a category as user (user.id 2)
         * Should be 401 Unauthorized
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + userToken)
                        .content("{\"name\": \"Nathan Category\", " +
                                "\"description\": \"Some lessons about Java\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Order(4)
    @Test
    public void testLessons() throws Exception {
        /*
         * Auth Admin
         * */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("rinaz_s", "password"));

        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());
        String adminToken = jwtUtils.generateToken(jwtUserDetails);

        /*
         * Post a lesson as admin (user.id 1)
         * Should be 201 Created
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/lesson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization" , "Bearer " + adminToken)
                        .content("{\"name\": \"This is my name\", " +
                                "\"title\": \"This is my title\"," +
                                "\"thumbnail\": \"path/to/img.png\", " +
                                "\"content\": \"This is my content\", " +
                                "\"categoryId\": \"1\"}"))
                .andExpect(status().isCreated());

        /*
         * Auth User
         * */
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("trenet_n", "password")
        );

        jwtUserDetails = jwtUserDetailsService.loadUserByUsername(authentication.getName());
        String userToken = jwtUtils.generateToken(jwtUserDetails);

        /*
         * Get lesson
         * Should be 200 Ok
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/lesson")
                        .header("Authorization" , "Bearer " + userToken))
                .andExpect(status().isOk());

        /*
         * Delete lesson with id 2 as user (id 2 is owned by Samir, not Nathan)
         * Should be 401 Unauthorized
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/lesson/2")
                        .header("Authorization" , "Bearer " + userToken))
                .andExpect(status().isUnauthorized());

        /*
         * Delete lesson with id 2 as admin (id 2 is owned by Samir)
         * Should be 200 Ok
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/lesson/2")
                        .header("Authorization" , "Bearer " + adminToken))
                .andExpect(status().isOk());

        /*
         * Get lessons without Bearer token
         * Should be 401 Unauthorized
         * */
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/lesson"))
                .andExpect(status().isUnauthorized());
    }
}
