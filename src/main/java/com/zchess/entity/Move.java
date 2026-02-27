package com.zchess.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Move {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private int fromSquare;
                    private int toSquare;
                        private String notation;

                            @Enumerated(EnumType.STRING)
                                private Color player;
                                }