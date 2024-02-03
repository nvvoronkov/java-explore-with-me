package practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.compilation.dto.CompilationDto;
import practicum.compilation.mapper.CompilationMapper;
import practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {

        log.info("Calling GET: /compilations with 'pinned': {}, 'from': {}, 'size': {}", pinned, from, size);
        return compilationMapper.listCompilationToListCompilationDto(compilationService.getAll(pinned, from, size));
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {

        log.info("Calling GET: /compilations/{compId} with 'compId': {}", compId);
        return compilationMapper.compilationToCompilationDto(compilationService.get(compId));
    }
}