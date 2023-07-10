window.addEventListener('load', function () {

    (function(){
      const url = '/turnos';
      const settings = {
        method: 'GET'
    }
    fetch(url,settings)
    .then(response => response.json())
    .then(data => {
            console.log(data)
         //recorremos la colección de turnos del JSON
         for(turno of data){
          //por cada turno armaremos una fila de la tabla
          //cada fila tendrá un id que luego nos permitirá borrar la fila si eliminamos
          //el turno

          var table = document.getElementById("turnoTable");
          var turnoRow =table.insertRow();
          let tr_id = 'tr_' + turno.id;
          turnoRow.id = tr_id;


          //por cada turno creamos un boton delete que agregaremos en cada fila para poder eliminar la misma
          //dicho boton invocara a la funcion de java script deleteBy(id) que se encargará
          //de llamar a la API para eliminar al turno
           let deleteButton = '<button' +
                                      ' id=' + '\"' + 'btn_delete_' + turno.id + '\"' +
                                      ' type="button" onclick="deleteBy('+turno.id+'); location.reload();" class="btn btn-danger btn_delete">' +
                                      '&times' +
                                      '</button>';

           //por cada turno creamos un boton que muestra el id y que al hacerle clic invocará
           //a la función de java script findBy que se encargará de buscar al turno que queremos
           //modificar y mostrar los datos del mismo en un formulario.
          let updateButton = '<button' +
                                      ' id=' + '\"' + 'btn_id_' + turno.id + '\"' +
                                      ' type="button" onclick="encontrarPorID('+turno.id+')" class="btn btn-info btn_id">' +
                                      turno.id +
                                      '</button>';


          //armamos cada columna de la fila
          //como primer columna pondremos el boton modificar
          //luego los datos del turno
          //como ultima columna el boton eliminar

         turnoRow.innerHTML = '<td>' + updateButton + '</td>' +
                              '<td class=\"td_nombre-paciente\">' + turno.nombrePaciente.toUpperCase() + '</td>' +
                              '<td class=\"td_apellido-paciente\">' + turno.apellidoPaciente.toUpperCase() + '</td>' +
                              '<td class=\"td_dni-paciente\">' + turno.dniPaciente.toUpperCase() + '</td>' +
                              '<td class=\"td_nombre-odontologo\">' + turno.nombreOdontologo.toUpperCase() + '</td>' +
                              '<td class=\"td_apellido-odontologo\">' + turno.apellidoOdontologo.toUpperCase() + '</td>' +
                              '<td class=\"td_fecha\">' + turno.fecha + '</td>' +
                              '<td class=\"td_hora\">' + turno.hora + '</td>' +
                              '<td>' + deleteButton + '</td>';

        };

})
})

(function(){
  let pathname = window.location.pathname;
  if (pathname == "/turnoList.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
  }
})


})