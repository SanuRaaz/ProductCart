package com.cartclothing.dev.cart.Controller;


import com.cartclothing.dev.cart.Exception.AlreadyExistException;
import com.cartclothing.dev.cart.Exception.CategoryNotFoundException;
import com.cartclothing.dev.cart.Modal.Category;
import com.cartclothing.dev.cart.Response.APIResponse;
import com.cartclothing.dev.cart.Service.Category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/category")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping(value= "/category/all")
    public ResponseEntity<APIResponse> getAllCategories()
    {
        try {
                List<Category> categories = categoryService.getAllCategory ();
                return ResponseEntity.ok(new APIResponse ("Category Found", categories));
             }
        catch ( Exception e )
        {
                  return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body (new APIResponse ("No Categories Found !", INTERNAL_SERVER_ERROR));
        }

    }


    @PostMapping(value = "/add")
    public ResponseEntity<APIResponse> addCategory(@RequestBody Category name)
    {
        try {
            Category theCategory = categoryService.addCategory (name);
            return ResponseEntity.ok(new APIResponse ("Success", theCategory));
        } catch ( AlreadyExistException e ) {
            return ResponseEntity.status(CONFLICT).body(new APIResponse (e.getMessage (), null));
        }
    }

    @GetMapping(value = "/category/{id}/category")
    public ResponseEntity<APIResponse> getCategoryById(@PathVariable Long id)
    {
        try {
            Category theCategory = categoryService.getCategoryById (id);
            return ResponseEntity.ok(new APIResponse ("Category Found", theCategory));
        } catch ( CategoryNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse (e.getMessage (), null));
        }
    }
//avoid the url at the same level
    @GetMapping(value = "/category/{name}/categoryname")
    public ResponseEntity<APIResponse> getCategoryByName(@PathVariable String name)
    {
        try {
            Category theCategory = categoryService.getCategoryByName (name);
            if(theCategory ==  null)
            {
                return ResponseEntity.status(NOT_FOUND).body (new APIResponse ("Not Found", null));
            }
            return ResponseEntity.ok(new APIResponse ("Category Found", theCategory));
        } catch ( CategoryNotFoundException e ) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResponse (e.getMessage (), null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<APIResponse> deleteCategoryById(@PathVariable Long id)
    {
        try {
                categoryService.deleteCategory (id);
                return ResponseEntity.ok(new APIResponse ("Deleted Succesfully", null));
        }
        catch ( CategoryNotFoundException e )
        {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse (e.getMessage (), null));
        }
    }
}
