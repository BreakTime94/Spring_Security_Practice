package com.climbjava.club.controller;
import com.climbjava.club.dto.NoteDTO;
import com.climbjava.club.service.NoteService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
  private final NoteService service;

  @PostMapping
  public ResponseEntity<Long> register(@RequestBody NoteDTO dto) {
    log.info(dto);
    Long num = service.register(dto);
    return ResponseEntity.ok(num);
  }

  @GetMapping(value = "/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<NoteDTO> get(@PathVariable Long num){
    log.info(num);
    service.get(num);
    return ResponseEntity.ok(service.get(num));
  }

  @GetMapping(value = "all", produces =  MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NoteDTO>> getAll(@RequestParam("email") String writerEmail, HttpSession httpSession) {
    String email = (String) httpSession.getAttribute("email");
    log.info(email);
    service.getAllWithWriter(writerEmail);
    
    return ResponseEntity.ok(service.getAllWithWriter(writerEmail));
  }

  @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> update(@RequestBody NoteDTO dto) {
    log.info(dto);
    log.info(dto.getNum());
    service.modify(dto);
    return ResponseEntity.ok("수정했다능");
  }

  @DeleteMapping(value = "{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> remove(@PathVariable("num") Long num) {
    log.info(num);

    service.remove(num);
    return ResponseEntity.ok("삭제했다능");
  }
}
