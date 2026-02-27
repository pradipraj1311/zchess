package com.zchess.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                @Enumerated(EnumType.STRING)
                    private Color currentTurn = Color.WHITE;

                        private boolean check;
                            private boolean checkmate;

                                @OneToMany(cascade = CascadeType.ALL)
                                    private List<Piece> pieces = new ArrayList<>();

                                        @OneToMany(cascade = CascadeType.ALL)
                                            private List<Move> moves = new ArrayList<>();
                                            }