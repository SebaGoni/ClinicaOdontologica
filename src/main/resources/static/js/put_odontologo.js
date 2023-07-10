window.addEventListener('load', function () {

    //Buscamos y obtenemos el formulario donde estan
    //los datos del odontologo
        const formulario = document.querySelector('#update_odontologo_form');
        formulario.addEventListener('submit', function (event) {
        let odontologoId = document.querySelector('#odontologo_id').value;
        event.preventDefault();
        //creamos un JSON que tendrá los datos del odontologo
        //a diferencia de un odontologo nuevo en este caso enviamos el id
        //para poder identificarlo y modificarlo para no cargarlo como nuevo

            var id = document.querySelector('#odontologo_id').value;
            var nombre = document.querySelector('#nombre').value;
            var apellido = document.querySelector('#apellido').value;
            var matricula = document.querySelector('#matricula').value;


                var datosOdontologo = {
                    "id": id,
                    "nombre": nombre,
                    "apellido": apellido,
                    "matricula": matricula
              };

              // Realizar la solicitud al servidor
              fetch('/odontologos', {
                method: "PUT",
                headers: {
                  "Content-Type": "application/json"
                },
                body: JSON.stringify(datosOdontologo)
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
                  '<strong></strong> Odontólogo modificado </div>';

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

    //Es la funcion que se invoca cuando se hace click sobre el id de un odontologo del listado
    //se encarga de llenar el formulario con los datos del odontologo
    //que se desea modificar
    function encontrarPorID(id) {
          const url = '/odontologos'+"/"+id;
          const settings = {
              method: 'GET'
          }
          fetch(url,settings)
          .then(response => response.json())
          .then(data => {
              let odontologo = data;
              document.querySelector('#odontologo_id').value = odontologo.id;
              document.querySelector('#apellido').value = odontologo.apellido;
              document.querySelector('#matricula').value = odontologo.matricula;
              document.querySelector('#nombre').value = odontologo.nombre;

            //el formulario por default esta oculto y al editar se habilita
              document.querySelector('#div_odontologo_updating').style.display = "block";
          }).catch(error => {
              alert("Error: " + error);
          })
      }