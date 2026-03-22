package com.zchess.controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zchess.entity.Move;
import com.zchess.service.MoveService;

@RestController
@RequestMapping("/api/games")
public class MoveController {

	private final MoveService moveService;

	public MoveController(MoveService moveService) {
		this.moveService = moveService;
	}

	@PostMapping("/{gameId}/move")
	public String[][] move(@PathVariable Long gameId, @RequestBody Move move) {

		return moveService.move(gameId,move);

	}
}