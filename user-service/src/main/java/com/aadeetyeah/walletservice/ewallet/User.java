package com.aadeetyeah.walletservice.ewallet;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true,nullable = false)  //unique
    private String email;

    @Column(unique = true,nullable = false)
    private String phoneNo;
    private int age;
    
}
