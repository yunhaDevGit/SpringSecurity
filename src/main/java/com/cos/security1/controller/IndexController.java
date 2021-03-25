package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // view return
public class IndexController {
  // mustache 기본 폴더 src/main/resources/
  // view resolver : templates (prefix), .mustache (suffix) 생략가능
  @GetMapping({"", "/"})
  public String index(){
    return "index"; // src/main/resources/templates/index.mustache
  }

}
