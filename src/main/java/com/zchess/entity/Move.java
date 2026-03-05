package com.zchess.entity;

import jakarta.persistence.*;

@Entity
public class Move {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                private String fromPosition;
                    private String toPosition;

                        private int moveNumber;

                            @ManyToOne
                                private Game game;

                                    public Move(){}

                                        public Long getId(){
                                                return id;
                                                    }

                                                        public String getFromPosition(){
                                                                return fromPosition;
                                                                    }

                                                                        public void setFromPosition(String fromPosition){
                                                                                this.fromPosition = fromPosition;
                                                                                    }

                                                                                        public String getToPosition(){
                                                                                                return toPosition;
                                                                                                    }

                                                                                                        public void setToPosition(String toPosition){
                                                                                                                this.toPosition = toPosition;
                                                                                                                    }

                                                                                                                        public int getMoveNumber(){
                                                                                                                                return moveNumber;
                                                                                                                                    }

                                                                                                                                        public void setMoveNumber(int moveNumber){
                                                                                                                                                this.moveNumber = moveNumber;
                                                                                                                                                    }

                                                                                                                                                        public Game getGame(){
                                                                                                                                                                return game;
                                                                                                                                                                    }

                                                                                                                                                                        public void setGame(Game game){
                                                                                                                                                                                this.game = game;
                                                                                                                                                                                    }
                                                                                                                                                                                    }