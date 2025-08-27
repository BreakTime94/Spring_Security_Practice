package com.climbjava.club.service;

import com.climbjava.club.dto.NoteDTO;
import com.climbjava.club.entity.Note;
import com.climbjava.club.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
  private final NoteRepository repository;

  @Override
  public Long register(NoteDTO dto) {
    Note note = dtoToEntity(dto);
    repository.save(note);
    return note.getNum();
  }

  @Override
  @PostAuthorize("returnObject.writerEmail == authentication.principal")
  public NoteDTO get(Long num) {
    return entityToDto(repository.findById(num).orElseThrow(() -> new RuntimeException("그런거 없음 ㅋ")));
  }

  @Override
  public void modify(NoteDTO dto) {
    Long num = dto.getNum();

   Note note = repository.findById(num).orElseThrow();
    note.changeTitle(dto.getTitle());
    note.changeContent(dto.getContent());
    repository.save(note);
  }

  @Override
  public void remove(Long num) {
    repository.deleteById(num);
  }

  @Override
  public List<NoteDTO> getAllWithWriter(String writerEmail) {
    List<Note> noteList = repository.getList(writerEmail);
    return noteList.stream().map(this::entityToDto).toList();
  }
}
