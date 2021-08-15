package com.example.itemservice.web.basic;

import com.example.itemservice.domain.Item;
import com.example.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping()
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam int quantity,
                       Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        // Item 객체에 그대로 담아 오기 때문에 Item 새로 만들 필요 없음
        // ModeAttribute의 (name="") 옵션 : model.addAttribute에 자동으로 들어감

        itemRepository.save(item);

        //model.addAttribute("item", item);
        return "basic/item";
    }

   // @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        // Item 객체에 그대로 담아 오기 때문에 Item 새로 만들 필요 없음
        // ModeAttribute의 (name="") 옵션 : model.addAttribute에 자동으로 들어감
        // Model Attribute에 담기는 원리(name지정 안할 시)
        //      : HelloData -> helloData
        //      : User -> user
        itemRepository.save(item);

        //model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4( Item item){
        itemRepository.save(item);

        return "redirect:/basic/items/"+item.getId();
    }

        @PostMapping("/add")
        public String addItemV5(Item item, RedirectAttributes redirectAttributes){
            // redirect할 때 parameter를 붙여서 보내서 확인 메시지를 띄운다
            Item savedItem = itemRepository.save(item);
            redirectAttributes.addAttribute("itemId", savedItem.getId());
            redirectAttributes.addAttribute("status", true);

            return "redirect:/basic/items/{itemId}";
            // query parameter가 포함되서 나간다 -> ?status=true;
        }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }

    //미리 메모리에 test용 item 추가
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
