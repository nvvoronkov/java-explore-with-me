package practicum.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import practicum.category.dto.CategoryDto;
import practicum.category.mapper.CategoryMapper;
import practicum.category.service.CategoryService;
import practicum.event.dto.NewEventDto;
import practicum.event.service.EventService;
import practicum.user.model.User;
import practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users/add-event")
@RequiredArgsConstructor
public class UserEventController {

    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public String newEventPage(Model model) {
        model.addAttribute("event", new NewEventDto());

        List<CategoryDto> selectCategoryOptions = categoryMapper.listCategoryToListCategoryDto(categoryService.getAll(0, 10));
        model.addAttribute("selectCategoryOptions", selectCategoryOptions);

        return "add-event";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addUserEvent(@ModelAttribute("event") @RequestBody @Valid NewEventDto event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userService.loadUserByUsername(authentication.getName());

        eventService.addUserEvent(currentUser.getId(), event);

        return "index";
    }
}
