window.addEventListener('load', function () {
  const formulario = document.querySelector('#add_new_turno');

  formulario.addEventListener("submit", function(event) {
    event.preventDefault();

    // Obtener los valores ingresados en el formulario
    var nombrePaciente = document.getElementById("nombre-paciente").value;
    var apellidoPaciente = document.getElementById("apellido-paciente").value;
    var dniPaciente = document.getElementById("dni-paciente").value;
    var nombreOdontologo = document.getElementById("nombre-odontologo").value;
    var apellidoOdontologo = document.getElementById("apellido-odontologo").value;
    var fecha = document.getElementById("fecha").value;
    var hora = document.getElementById("hora").value;

    // Obtener los IDs del odontólogo y el paciente del servidor
obtenerIdOdontologo(nombreOdontologo, apellidoOdontologo)
  .then(function(idOdontologo) {
    if (idOdontologo == null) {
      return;
    }
    return obtenerIdPaciente(nombrePaciente, apellidoPaciente, dniPaciente)
      .then(function(idPaciente) {
        if (idPaciente == null) {
          return;
        }
        // Crear el objeto de datos del turno con los IDs obtenidos
        var datosTurno = {
          "odontologo": { "id": idOdontologo },
          "paciente": { "id": idPaciente },
          "fecha": fecha,
          "hora": hora
        };

        // Realizar la solicitud al servidor
        fetch('/turnos', {
          method: "POST",
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
          // Realizo las acciones necesarias con la respuesta del servidor
          let successAlert = '<div class="alert alert-success alert-dismissible">' +
            '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
            '<strong></strong> Turno agregado </div>';

          document.querySelector('#response').innerHTML = successAlert;
          document.querySelector('#response').style.display = "block";
          // Se dejan todos los campos vacíos por si se quiere ingresar otro turno
          resetUploadForm();
        })
        .catch(error => {
          let errorMessage = error.message;
          let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
            '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
            '<strong>Error: </strong>' + errorMessage + '</div>';

          document.querySelector('#response').innerHTML = errorAlert;
          document.querySelector('#response').style.display = "block";
        });
      });
  });




  });

  function resetUploadForm() {
    document.querySelector('#nombre-paciente').value = "";
    document.querySelector('#apellido-paciente').value = "";
    document.querySelector('#dni-paciente').value = "";
    document.querySelector('#nombre-odontologo').value = "";
    document.querySelector('#apellido-odontologo').value = "";
    document.querySelector('#fecha').value = "";
    document.querySelector('#hora').value = "";
  }

  (function() {
    let pathname = window.location.pathname;
    if (pathname === "/") {
      document.querySelector(".nav .nav-item a:first").addClass("active");
    } else if (pathname == "/turnoList.html") {
      document.querySelector(".nav .nav-item a:last").addClass("active");
    }
  })();

//   Función para obtener el ID del odontólogo desde el servidor (se usa el nombre y apellido que ingreso el usuario en el FORM
//   dado que la matricula no la deberia conocer. Se considera que hay solo un odontologo con el mismo nombre y apellido caso contrario
//   habria que modificar el codigo para que acepte tambien la matricula y nos devuelva un solo odontologo)

  function obtenerIdOdontologo(nombreOdontologo, apellidoOdontologo) {
    return fetch('/odontologos/idPorNombreyApellido?nombre=' + encodeURIComponent(nombreOdontologo) + '&apellido=' + encodeURIComponent(apellidoOdontologo))
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
      .catch(function(error) {
        console.error("Error al obtener el ID del odontólogo:", error.message);
                let errorMessage = error.message;
                let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                  '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                  '<strong>Error: </strong>' + errorMessage + '</div>';

                document.querySelector('#response').innerHTML = errorAlert;
                document.querySelector('#response').style.display = "block";
                return null;
      });
  }


  function obtenerIdPaciente(nombrePaciente, apellidoPaciente, dniPaciente) {
    return fetch('/pacientes/nombre_Apellido_dni?nombre=' + encodeURIComponent(nombrePaciente) + '&apellido=' + encodeURIComponent(apellidoPaciente) + '&dni=' + encodeURIComponent(dniPaciente))
      .then(function(response) {
        if (response.ok) {
          return response.json();
        } else if (response.status === 404) {
          return response.json().then(function(errorData) {
            let errorMessage = errorData.message || "Error en la solicitud";
            throw new Error(errorMessage);
          });
        } else {
          throw new Error("Error en la solicitud");
        }
      })
      .catch(function(error) {
        console.error("Error al obtener el ID del paciente:", error.message);

        let errorMessage = error.message;
        let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
          '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
          '<strong>Error: </strong>' + errorMessage + '</div>';

        document.querySelector('#response').innerHTML = errorAlert;
        document.querySelector('#response').style.display = "block";
        return null;
      });
  }



});


