    window.addEventListener('load', function () {

    const formulario = document.querySelector('#update_turno_form');

    formulario.addEventListener('submit', function (event) {
        event.preventDefault();

      // Obtener los valores ingresados en el formulario
      var id = document.getElementById("turno_id").value
      var nombrePaciente = document.getElementById("nombrePaciente").value;
      var apellidoPaciente = document.getElementById("apellidoPaciente").value;
      var dniPaciente = document.getElementById("dniPaciente").value;
      var nombreOdontologo = document.getElementById("nombreOdontologo").value;
      var apellidoOdontologo = document.getElementById("apellidoOdontologo").value;
      var fecha = document.getElementById("fecha").value;
      var hora = document.getElementById("hora").value;

      // Obtener los IDs del odontólogo y el paciente del servidor
obtenerIdOdontologo(nombreOdontologo, apellidoOdontologo)
  .then(function(idOdontologo) {
    if (idOdontologo === null) {
      // Detener la ejecución o manejar el caso de error
      return;
    }

    return obtenerIdPaciente(nombrePaciente, apellidoPaciente, dniPaciente)
      .then(function(idPaciente) {
        if (idPaciente === null) {
          // Detener la ejecución o manejar el caso de error
          return;
        }

        // Crear el objeto de datos del turno con los IDs obtenidos
        var datosTurno = {
          "id": id,
          "odontologo": { "id": idOdontologo },
          "paciente": { "id": idPaciente },
          "fecha": fecha,
          "hora": hora
        };

        // Realizar la solicitud al servidor
        fetch('/turnos', {
          method: "PUT",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(datosTurno)
        })
          .then(function(response) {
            if (response.ok) {
              return response.json();
            } else {
              return response.json().then(function(errorData) {
                let errorMessage = errorData.message || "Error en la solicitud";
                throw new Error(errorMessage);
              });
            }
          })
          .then(function(respuestaServidor) {
            console.log(respuestaServidor);
            // Realizar las acciones necesarias con la respuesta del servidor
            // location.reload(true);
            let successAlert = '<div class="alert alert-success alert-dismissible">' +
              '<button type="button" class="close" onclick="location.reload()" data-dismiss="alert">&times;</button>' +
              '<strong></strong> Turno modificado </div>';

            document.querySelector('#response').innerHTML = successAlert;
            document.querySelector('#response').style.display = "block";
          })
          .catch(function(error) {
            let errorMessage = error.message;
            let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
              '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
              '<strong>Error: </strong>' + errorMessage + '</div>';

            document.querySelector('#response').innerHTML = errorAlert;
            document.querySelector('#response').style.display = "block";
          });
      });
  });

    })
 })

    //Es la funcion que se invoca cuando se hace click sobre el id de un turno del listado
    //se encarga de llenar el formulario con los datos del turno
    //que se desea modificar
    function encontrarPorID(id) {
          const url = '/turnos'+"/"+id;
          const settings = {
              method: 'GET'
          }
          fetch(url,settings)
          .then(response => response.json())
          .then(data => {
              let turno = data;
              document.querySelector('#turno_id').value = turno.id;
              document.querySelector('#hora').value = turno.hora;
              document.querySelector('#fecha').value = turno.fecha;
              document.querySelector('#nombrePaciente').value = turno.nombrePaciente;
              document.querySelector('#apellidoPaciente').value = turno.apellidoPaciente;
              document.querySelector('#dniPaciente').value = turno.dniPaciente;
//              document.querySelector('#idPaciente').value = turno.idPaciente;
              document.querySelector('#nombreOdontologo').value = turno.nombreOdontologo;
              document.querySelector('#apellidoOdontologo').value = turno.apellidoOdontologo;

            //el formulario por default esta oculto y al editar se habilita
              document.querySelector('#div_turno_updating').style.display = "block";
          }).catch(error => {
              alert("Error: " + error);
          })
      }

      // Función para obtener el ID del odontólogo desde el servidor
          function obtenerIdOdontologo(nombreOdontologo, apellidoOdontologo) {
                        return fetch('/odontologos/idPorNombreyApellido?nombre=' + encodeURIComponent(nombreOdontologo) + '&apellido=' + encodeURIComponent(apellidoOdontologo))

                        .then(function(response) {
                          if (response.ok) {
                            return response.json();
                          } else {
                             return response.json().then(function(errorData) {
                                      throw new Error(errorData.message || 'Error en la solicitud');
                                    });
                          }
                        })
                        .catch(function(error) {
                          let errorMessage = error.message;
                          let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                            '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                            '<strong>Error: </strong>' + errorMessage + '</div>';

                          document.querySelector('#response').innerHTML = errorAlert;
                          document.querySelector('#response').style.display = "block";
                          return null;
                      });

          }

 // Función para obtener el ID del paciente desde el servidor
 function obtenerIdPaciente(nombrePaciente, apellidoPaciente, dniPaciente) {
   // Realizar la solicitud al servidor para obtener el ID del paciente
   return fetch('/pacientes/nombre_Apellido_dni?nombre=' + encodeURIComponent(nombrePaciente) + '&apellido=' + encodeURIComponent(apellidoPaciente) + '&dni=' + encodeURIComponent(dniPaciente))
                             .then(function(response) {
                               if (response.ok) {
                                 return response.json();
                               } else {
                                  return response.json().then(function(errorData) {
                                           throw new Error(errorData.message || 'Error en la solicitud');
                                         });
                               }
                             })

                             .catch(function(error) {
                               let errorMessage = error.message;
                               let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                                 '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                                 '<strong>Error: </strong>' + errorMessage + '</div>';

                               document.querySelector('#response').innerHTML = errorAlert;
                               document.querySelector('#response').style.display = "block";
                               return null;
                           });
 }