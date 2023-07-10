window.addEventListener('load', function () {

    //Buscamos y obtenemos el formulario donde estan
    //los datos que el usuario pudo haber modificado del paciente
    const formulario = document.querySelector('#update_paciente_form');
    formulario.addEventListener('submit', function (event) {
        let pacienteId = document.querySelector('#paciente_id').value;
        event.preventDefault();
        //creamos un JSON que tendrá los datos del paciente
        //a diferencia de un paciente nuevo en este caso enviamos el id
        //para poder identificarlo y modificarlo para no cargarlo como nuevo

            var paciente_id = document.querySelector('#paciente_id').value;
            var nombre = document.querySelector('#nombre').value;
            var apellido = document.querySelector('#apellido').value;
            var dni = document.querySelector('#dni').value;
            var fechaDeAlta = document.querySelector('#fechaDeAlta').value;
            var domicilio_id = document.querySelector('#domicilio_id').value;
            var calle = document.querySelector('#calle').value;
            var nro = document.querySelector('#nro').value;
            var localidad = document.querySelector('#localidad').value;
            var provincia = document.querySelector('#provincia').value;


            var datosPaciente = {
                                "id": paciente_id,
                                "nombre": nombre,
                                "apellido": apellido,
                                "dni": dni,
                                "fechaDeAlta": fechaDeAlta,
                                "domicilio": {
                                            "id": domicilio_id,
                                            "calle": calle,
                                            "nro": nro,
                                            "localidad": localidad,
                                            "provincia": provincia
                                }
                          };


        //invocamos utilizando la función fetch la API pacientes con el método PUT
        //que modificará al paciente que enviaremos en formato JSON
              fetch('/pacientes', {
                method: "PUT",
                headers: {
                  "Content-Type": "application/json"
                },
                body: JSON.stringify(datosPaciente)
              })
              .then(function(response) {
                if (response.ok) {
                  return response.json();
                } else {
                   return response.json().then(function(errorData) {
                            throw new Error(errorData.message || 'Error en la solicitud');
                          });
                }
              })
              .then(function(respuestaServidor) {
                // Realizar las acciones necesarias con la respuesta del servidor
                let successAlert = '<div class="alert alert-success alert-dismissible">' +
                  '<button type="button" class="close" onclick="location.reload()" data-dismiss="alert">&times;</button>' +
                  '<strong></strong> Paciente modificado </div>';

                document.querySelector('#response').innerHTML = successAlert;
                document.querySelector('#response').style.display = "block";
//                 resetUploadForm();

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

    //Es la funcion que se invoca cuando se hace click sobre el id de un paciente del listado
    //se encarga de llenar el formulario con los datos del paciente
    //que se desea modificar
    function encontrarPorID(id) {
          const url = '/pacientes'+"/"+id;
          const settings = {
              method: 'GET'
          }
          fetch(url,settings)
          .then(response => response.json())
          .then(data => {
              let paciente = data;
              document.querySelector('#paciente_id').value = paciente.id;
              document.querySelector('#nombre').value = paciente.nombre;
              document.querySelector('#apellido').value = paciente.apellido;
              document.querySelector('#dni').value = paciente.dni;
              document.querySelector('#fechaDeAlta').value = paciente.fechaDeAlta;
              document.querySelector('#domicilio_id').value = paciente.domicilio.id;
              document.querySelector('#calle').value = paciente.domicilio.calle;
              document.querySelector('#nro').value = paciente.domicilio.nro;
              document.querySelector('#localidad').value = paciente.domicilio.localidad;
              document.querySelector('#provincia').value = paciente.domicilio.provincia;

            //el formulario por default esta oculto y al editar se habilita
              document.querySelector('#div_paciente_updating').style.display = "block";
          }).catch(error => {
              alert("Error: " + error);
          })
      }