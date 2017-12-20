package com.ttn.blogQuotient.service;

import com.ttn.blogQuotient.dto.CommonResponseDTO;
import com.ttn.blogQuotient.dto.UserDetails;

public interface UserService {

    public CommonResponseDTO registerUser(UserDetails userDetails);

}
