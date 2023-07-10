window.addEventListener('load', function () {

     //Al cargar la pagina buscamos y obtenemos el formulario donde estarán
     //los datos que el usuario cargará del nuevo paciente
    const formulario = document.querySelector('#add_new_paciente');

    //Ante un submit del formulario se ejecutará la siguiente funcion
    formulario.addEventListener('submit', function (event) {
        event.preventDefault()
        //creamos un JSON que tendrá los datos del nuevo paciente
        const formData = {
                  nombre: document.querySelector('#nombre').value,
                  apellido: document.querySelector('#apellido').value,
                  dni: document.querySelector('#dni').value,
                  fechaDeAlta: document.querySelector('#fechaDeAlta').value,
                  domicilio: {
                    calle: document.querySelector('#calle').value,
                    nro: document.querySelector('#nro').value,
                    localidad: document.querySelector('#localidad').value,
                    provincia: document.querySelector('#provincia').value
                  }
        };

        //invocamos utilizando la función fetch la API pacientes con el método POST
        //que guardará al paciente que enviaremos en formato JSON
        const url = '/pacientes';
        const settings = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        }

        fetch(url, settings)
            .then(response => {
                    if (!response.ok) {
                        return response.json().then(errorData => {
                            throw new Error(JSON.stringify(errorData));
                        });
                    }
                    return response.json();
                })
            .then(data => {
               //Si no hay ningun error se muestra un mensaje diciendo que el paciente
               //se agrego bien
                 let successAlert = '<div class="alert alert-success alert-dismissible">' +
                     '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                     '<strong></strong> Paciente agregado </div>'

                 document.querySelector('#response').innerHTML = successAlert;
                 document.querySelector('#response').style.display = "block";
                 //se dejan todos los campos vacíos por si se quiere ingresar otro paciente
                 resetUploadForm();

            })
            .catch(error => {
                let errorMessage = JSON.parse(error.message);

                let errorAlert = '<div class="alert alert-danger alert-dismissible">' +
                 '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
                 '<strong>Error: </strong>' + errorMessage.message + '</div>';

             document.querySelector('#response').innerHTML = errorAlert;
             document.querySelector('#response').style.display = "block";

            });
    });

    function resetUploadForm(){
        document.querySelector('#nombre').value = "";
        document.querySelector('#apellido').value = "";
        document.querySelector('#dni').value = "";
        document.querySelector('#fechaDeAlta').value = "";
        document.querySelector('#calle').value = "";
        document.querySelector('#nro').value = "";
        document.querySelector('#localidad').value = "";
        document.querySelector('#provincia').value = "";
    }

    (function(){
        let pathname = window.location.pathname;
        if(pathname === "/"){
            document.querySelector(".nav .nav-item a:first").addClass("active");
        } else if (pathname == "/pacienteList.html") {
            document.querySelector(".nav .nav-item a:last").addClass("active");
        }
    })();
});