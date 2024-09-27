package com.lcwd.electronicstore.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import com.lcwd.electronicstore.dto.PageableResponse;

public class HelperUtility {
	public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){
		List<U>  entity = page.getContent();
		List<V>  userDtoList = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());
		PageableResponse<V> response=new PageableResponse<>();
		response.setContent(userDtoList);
		response.setLastPage(page.isLast());
		response.setPageNumebr(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		return response;

	}
}
