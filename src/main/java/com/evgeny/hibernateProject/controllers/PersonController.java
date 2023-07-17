package com.evgeny.hibernateProject.controllers;

import com.evgeny.hibernateProject.models.Person;
import com.evgeny.hibernateProject.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personService.findAll());
        return "personPages/index";
    }

    @GetMapping("/new")
    public String creatingPersonForm(@ModelAttribute("person") Person person) {
        return "personPages/creatingPersonPage";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "personPages/creatingPersonPage";
        }
        personService.save(person);
        return "redirect:/person";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("personToEdit", personService.findById(id));
        return "personPages/editPerson";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "personPages/editPerson";
        }
        personService.updatePerson(person);
        return "redirect:/person";
    }

    @GetMapping("/{id}")
    public String personPage(@PathVariable("id") int id, Model model) {
        model.addAttribute("ownerOfPage", personService.findById(id));
        model.addAttribute("ownerBooksList", personService.findByIdAndGetBooks(id));
        return "personPages/personPage";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deleteById(id);
        return "redirect:/person";
    }
}
