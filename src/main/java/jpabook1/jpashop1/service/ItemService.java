package jpabook1.jpashop1.service;

import jpabook1.jpashop1.domain.Item;
import jpabook1.jpashop1.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findALl();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    @Transactional
    public void updateItem(Long itemid, String name, int price) {
        Item findItem = itemRepository.findOne(itemid);
        findItem.setName(name);
        findItem.setPrice(price);
    }

}
