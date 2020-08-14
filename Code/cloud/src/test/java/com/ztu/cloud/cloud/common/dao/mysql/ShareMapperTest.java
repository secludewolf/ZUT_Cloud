package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.Share;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareMapperTest {
	@Autowired
	ShareMapper shareMapper;

	@Test
	void getShareById() {
		this.shareMapper.insertShare(new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		System.out.println(this.shareMapper.getShareById("test"));
		this.shareMapper.deleteShareById("test");
	}

	@Test
	void getShareByUserId() {
		this.shareMapper.insertShare(new Share("test1", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		this.shareMapper.insertShare(new Share("test2", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		System.out.println(this.shareMapper.getShareByUserId(1));
		this.shareMapper.deleteShareById("test1");
		this.shareMapper.deleteShareById("test2");
	}

	@Test
	void getShare() {
		// this.shareMapper.insertShare(new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		// System.out.println(this.shareMapper.getShare(0, 2));
		// this.shareMapper.deleteShareById("test");
	}

	@Test
	void getShareCount() {
		this.shareMapper.insertShare(new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		System.out.println(this.shareMapper.getShareCount());
		this.shareMapper.deleteShareById("test");
	}

	@Test
	void updateShare() {
		Share share = new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis());
		this.shareMapper.insertShare(share);
		System.out.println(share);
		share.setPassword("1234qwer");
		this.shareMapper.updateShare(share);
		System.out.println(this.shareMapper.getShareById(share.getId()));
		this.shareMapper.deleteShareById(share.getId());
	}

	@Test
	void insertShare() {
		this.shareMapper.insertShare(new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		System.out.println(this.shareMapper.getShareById("test"));
		this.shareMapper.deleteShareById("test");
	}

	@Test
	void deleteShareById() {
		this.shareMapper.insertShare(new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		System.out.println(this.shareMapper.deleteShareById("test"));
	}

	@Test
	void updateShareStatus() {
		Share share = new Share("test", 1, "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis());
		this.shareMapper.insertShare(share);
		System.out.println(share);
		this.shareMapper.updateShareStatus(share.getId(), 0);
		System.out.println(this.shareMapper.getShareById(share.getId()));
		this.shareMapper.deleteShareById(share.getId());
	}
}