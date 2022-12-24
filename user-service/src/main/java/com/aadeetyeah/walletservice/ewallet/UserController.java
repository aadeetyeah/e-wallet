package com.aadeetyeah.walletservice.ewallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // Primitive way
//    @GetMapping("/user")
//    public User getUser(@RequestParam("id") int id){
//        return userService.getUser(id);
//    }
//
//    @GetMapping("/user/email")
//    public User getUserByMail(@RequestParam("email") String mail){
//        return userService.getUserByMail(mail);
//    }

    //Scalable way
    @GetMapping("/user")
    public User getUser(@RequestParam("search_type")String searchType,
                        @RequestParam("search_value")String searchValue) throws Exception {
        UserVectorEnum userVectorEnum=UserVectorEnum.valueOf(searchType);
        switch (userVectorEnum){
            case ID:
                return userService.getUser(Integer.parseInt(searchValue));
            case EMAIL:
                return userService.getUserByMail(searchValue);
            default:
                throw new Exception("Invalid search type found.");
        }
    }

    @PostMapping("/user")
    public void createUser(@RequestBody User user){
        userService.createUser(user);
    }

}
