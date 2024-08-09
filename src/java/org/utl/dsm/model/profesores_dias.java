    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package org.utl.dsm.model;

    /**
     *
     * @author bryan
     */
    public class profesores_dias {
        private Profesor profesor;
        private DiaDisponible dias_disponibles;
        private String horaInicio;
       private String horaFin;

        public profesores_dias() {
        }

        public profesores_dias(Profesor profesor, DiaDisponible dias_disponibles, String horaInicio, String horaFin) {
            this.profesor = profesor;
            this.dias_disponibles = dias_disponibles;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
        }

        public String getHoraFin() {
            return horaFin;
        }

        public void setHoraFin(String horaFin) {
            this.horaFin = horaFin;
        }

        public Profesor getProfesor() {
            return profesor;
        }

        public void setProfesor(Profesor profesor) {
            this.profesor = profesor;
        }

        public DiaDisponible getDias_disponibles() {
            return dias_disponibles;
        }

        public void setDias_disponibles(DiaDisponible dias_disponibles) {
            this.dias_disponibles = dias_disponibles;
        }

        public String getHoraInicio() {
            return horaInicio;
        }

        public void setHoraInicio(String horaInicio) {
            this.horaInicio = horaInicio;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("profesores_dias{");
            sb.append("profesor:").append(profesor);
            sb.append(", dias_disponibles:").append(dias_disponibles);
            sb.append(", horaInicio:").append(horaInicio);
            sb.append(", horaFin:").append(horaFin);
            sb.append('}');
            return sb.toString();
        }



    }