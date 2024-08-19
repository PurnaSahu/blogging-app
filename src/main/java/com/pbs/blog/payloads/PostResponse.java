package com.pbs.blog.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostResponse {

	/*the data which we fetched from DB, earlier we were passing it directly to client, but now rather than passing it to client
	    directly, now we are keeping it in a response class and will return this class object.
	 */
	private List<PostDto> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	
	private boolean lastPage;
}
