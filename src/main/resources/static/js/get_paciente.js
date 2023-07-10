window.addEventListener('load', function () {

    (function(){
      const url = '/pacientes';
      const settings = {
        method: 'GET'
    }
    fetch(url,settings)
    .then(response => response.json())
    .then(data => {
            console.log(data)
         //recorremos la colección de pacientes del JSON
         for(paciente of data){
          //por cada paciente armaremos una fila de la tabla
          //cada fila tendrá un id que luego nos permitirá borrar la fila si eliminamos
          //el paciente

          var table = document.getElementById("pacienteTable");
          var pacienteRow =table.insertRow();
          let tr_id = 'tr_' + paciente.id;
          pacienteRow.id = tr_id;


          //por cada paciente creamos un boton delete que agregaremos en cada fila para poder eliminar la misma
          //dicho boton invocara a la funcion de java script deleteBy(id) que se encargará
          //de llamar a la API para eliminar al paciente
           let deleteButton = '<button' +
                                      ' id=' + '\"' + 'btn_delete_' + paciente.id + '\"' +
                                      ' type="button" onclick="deleteBy('+paciente.id+')" class="btn btn-danger btn_delete">' +
                                      '&times' +
                                      '</button>';

           //por cada paciente creamos un boton que muestra el id y que al hacerle clic invocará
           //a la función de java script findBy que se encargará de buscar al paciente que queremos
           //modificar y mostrar los datos del mismo en un formulario.
          let updateButton = '<button' +
                                      ' id=' + '\"' + 'btn_id_' + paciente.id + '\"' +
                                      ' type="button" onclick="encontrarPorID('+paciente.id+')" class="btn btn-info btn_id">' +
                                      paciente.id +
                                      '</button>';


          //armamos cada columna de la fila
          //como primer columna pondremos el boton modificar
          //luego los datos del paciente
          //como ultima columna el boton eliminar

         pacienteRow.innerHTML = '<td>' + updateButton + '</td>' +
                              '<td class=\"td_nombre\">' + paciente.nombre.toUpperCase() + '</td>' +
                              '<td class=\"td_apellido\">' + paciente.apellido.toUpperCase() + '</td>' +
                              '<td class=\"td_dni\">' + paciente.dni + '</td>' +
                              '<td class=\"td_fechaDeAlta\">' + paciente.fechaDeAlta + '</td>' +
                              '<td class=\"td_domicilioId\">' + paciente.domicilio.id + '</td>' +
                              '<td class=\"td_domicilioCalle\">' + paciente.domicilio.calle.toUpperCase() + '</td>' +
                              '<td class=\"td_domicilioNro\">' + paciente.domicilio.nro + '</td>' +
                              '<td class=\"td_domicilioLocalidad\">' + paciente.domicilio.localidad.toUpperCase() + '</td>' +
                              '<td class=\"td_domicilioProvincia\">' + paciente.domicilio.provincia.toUpperCase() + '</td>' +
                              '<td>' + deleteButton + '</td>';

        };

})
})

(function(){
  let pathname = window.location.pathname;
  if (pathname == "/pacienteList.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
  }
})


})