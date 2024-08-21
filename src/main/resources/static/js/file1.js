const html = `
<tr>
<!--update/write-->
    <th>파일</th>
    <div id="preview">
        <div th:if="${fileList != null}" th:each="file : ${fileList}">
            <p th:text="${file.fileOriginalName}"></p>
            <button th:onclick="|loaction.href='@{/download/{fileId}(fileId=${file.fileId})}'|">download</button>
            <button th:attr="data-id=${file.fileId}" className="add-remove-file-list">X</button>
        </div>
    </div>
    <p>
        <input type="file" name="files" id="file-input" multiple/>
    </p>
    
    
        <!-- view -->
        <th scope="row">파일</th>
        <td colspan="3" th:if="${fileList != null}" th:each="file : ${fileList}">
          <a th:href="@{/download/{fileId}(fileId=${file.fileId})}"
             th:text="${file.fileOriginalName}"></a>
        </td>
</tr>`;

//파일 처리
const fileInput = $('#file-input');
const preview = $('#preview');

let removeFileList = [];

// 파일 추가
$(document).on('change', fileInput, (e) => {
    const fileList = e.target.files;
    $(fileList).each((index, file) => {
        const lastModified = file.lastModified;
        const name = file.name;

        const view = '<p id="' + lastModified + '">' + name +
            '<button data-index="' + lastModified +
            '" class="file-remove">X</button></p>';

        preview.append(view);
    });
});

// 파일 제거
$(document).on('click', '.file-remove', (e) => {
    e.preventDefault();

    const removeTargetId = e.target.dataset.index;
    const removeTarget = document.getElementById(removeTargetId);
    const fileList = fileInput[0].files;
    const dataTransfer = new DataTransfer();

    $(fileList).filter((index, file) => file.lastModified != removeTargetId)
        .each((index, file) => {
            dataTransfer.items.add(file);
        });

    fileInput[0].files = dataTransfer.files;

    removeTarget.remove();
});

$(document).on('click', '.add-remove-file-list', (e) => {
    e.preventDefault();

    const fileId = e.target.dataset.id;

    console.log(removeFileList);

    if (!removeFileList.includes(fileId)) {
        removeFileList.push(fileId);
        $(e.target).parent().css("opacity", "0.5");
    } else {
        removeFileList = removeFileList.filter((element) => element !== fileId);
        $(e.target).parent().css("opacity", "1");
    }
    console.log(removeFileList);
});

$(document).on('submit', '#update', (e) => {
    removeFileList.forEach((element) => {
        let add = `<input type="hidden" name="removeFileList" value="` + element + `">`;
        $('#remove-file-list').append(add);
    })
})